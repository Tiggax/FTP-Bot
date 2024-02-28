import concurrent.futures
import re
import subprocess

first = 0
second = 0

COMMAND_START = ["java", "-jar", "Game.jar"]
COMMAND_END = ["-gui=false"]



LINES_PER_PLAYER = 18
ARGS_POS = [("Player1/class", 4), ("Player2/class", 2), ("Player3/class", 3), ("Player4/class", 1)]



def extract_survive_value(input_string):
    match = re.search(r'survive: (\w+)', input_string)
    if match:
        return match.group(1).lower() == 'true'
    else:
        return False


def run_jar(thread_id, args):
   
    global first, second

    process = subprocess.Popen(COMMAND_START + [item[0] for item in args] + COMMAND_END, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    
    output, error = process.communicate()
    lines = output.split('\n')
    
    if len(lines) >= LINES_PER_PLAYER:
        if extract_survive_value(lines[-(LINES_PER_PLAYER * args[0][1] - 1)]) or extract_survive_value(lines[-(LINES_PER_PLAYER * args[1][1] - 1)]): first += 1
        if extract_survive_value(lines[-(LINES_PER_PLAYER * args[2][1] - 1)]) or extract_survive_value(lines[-(LINES_PER_PLAYER * args[3][1] - 1)]): second += 1
    
    print("------------ Id: " + str(thread_id))
    print("F: " + str(first))
    print("S: " + str(second))
    print(error + "\n")




if __name__ == "__main__":

    n_times = 50
    at_same_time = 16

    with concurrent.futures.ThreadPoolExecutor(max_workers=at_same_time) as executor:
        for i in range(int(n_times)):
            ARGS_POS.reverse()
            executor.submit(run_jar, i + 1, ARGS_POS)



    print("End.\n")
    print("All: " + str(n_times))
    print("F: " + str(first) + " R: " + str(first / n_times))
    print("S: " + str(second) + " R: " + str(second / n_times))
    print("Tie: " + str(n_times - (second + first)) + " R: " + str((n_times - (second + first)) / n_times))
    print("\nAll threads have completed.")
