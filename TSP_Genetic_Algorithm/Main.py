import random
import CityMap
import Initialization
import Crossover
import Mutation
import Selection
import copy

def main():

    western29 = "Cities/TSP_WesternSahara_29.txt"
    uruguay734 = "Cities/TSP_Uruguay_734.txt"
    canada4663 = "Cities/TSP_Canada_4663.txt"

    popsize = 1200
    mating_pool_size = 600
    tournament_size = 3
    mut_rate = 0.2
    xover_rate = 0.9
    gen_limit = 300

    print("Preparing for the information...")
    c = CityMap.CityMap(western29)
    city_map = c.city_map
    print("preparation end.")

    gen = 0
    print("Initialize population...")
    init = Initialization.Population(popsize, city_map)
    init.evalPopulation()
    print("Initialization end.")

    #EA algorithm
    while gen < gen_limit:

        #parent selection
        #print("parent selection...")
        parents = Selection.tournament_selection(init.population[1:], mating_pool_size, tournament_size)
        #parents = Selection.random_selection(init.population[1:], mating_pool_size)
        #parents = Selection.MPS(init.population[1:], mating_pool_size)

        offsprings = []
        i = 0
        while len(offsprings) < mating_pool_size:    # 2 parents  ->  2 individuals
            p1 = parents[i]
            p2 = parents[i + 1]

            # crossover ################################################################################################
            #print("crossover...")
            if random.random() < xover_rate:
                #off1, off2 = Crossover.COWGC(p1, p2, city_map)
                off1, off2 = Crossover.order_crossover(p1, p2, city_map)
            else:
                off1 = copy.copy(p1)
                off2 = copy.copy(p2)
            #print("crossover end")

            # mutation #################################################################################################
            #print("Mutation...")
            if random.random() < mut_rate:
                off1 = Mutation.WGWWGM(p1, city_map)
                #off1 = Mutation.WGWRGM(p1, city_map)
                #off1 = Mutation.IRGIBNNM_mutation(p1, city_map)
                #off1 = Mutation.inversion_mutation(p1, city_map)
            if random.random() < mut_rate:
                off2 = Mutation.WGWWGM(p2, city_map)
                #off2 = Mutation.WGWRGM(p2, city_map)
                #off2 = Mutation.IRGIBNNM_mutation(p2, city_map)
                #off2 = Mutation.inversion_mutation(p2, city_map)
            #print("Mutation end")

            offsprings.append(off1)
            offsprings.append(off2)
            #print(len(offsprings))

            i += 2

        # survial selection ############################################################################################
        #print("survival selection")
        init.population = Selection.mu_plus_lambda(init.population, offsprings)

        init.evalPopulation()

        print("generation:", gen, " Average length:", init.AverageLength, " Longest length: ", init.worstTour.length, " shortest length:", init.bestTour.length)

        gen += 1


main()
