
from statistics import stdev, mean
import csv

"""
This is a small program that use to calculate needed statistic information
"""
if __name__ == "__main__":

    stat= {"best_sd" : None, "worst_sd": None}
    best_in_trials = []
    worst_in_trials = []
    average_in_trials = []
    success_Western = 27602
    success_Uruguary = 80000

    #gen = 300  # generation for Western
    gen = 2000 # generation for Uruguay

    with open("statFile.csv", "r") as file:
        reader = csv.reader(file)
        for row in reader:
            if row[0] == "best":
                best = []
                for i in row[1:]:
                    best.append(float(i))
                m = min(best)
                best_in_trials.append(m)

            elif row[0] == "worst":
                worst = []
                for i in row[1:]:
                    worst.append(float(i))
                w = max(worst)
                worst_in_trials.append(w)


    print("trials", len(best_in_trials))

    # calculate best tour standard deviation among trials
    stat["best_sd"] = stdev(best_in_trials)
    stat["worst_sd"] = stdev(worst_in_trials)

    print("sd for best tours in trials", stat["best_sd"])
    print("sd for worst tours in trials", stat["worst_sd"])

    # calculate successful rate and MBF
    success = 0

    for i in best_in_trials:
        if i <= success_Western:
            success += 1

    sr = success/10
    mbf = mean(best_in_trials)

    print("success rate: ", sr)
    print("mean of best tours over all trials", mbf)




