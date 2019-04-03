import random

"""
A file that contains parent selection and survival selection
parent selection: tournament selection
survival selection: mu plus lambda selection
"""

def tournament_selection(population, mating_pool_size, tournament_size):
    """
    Tournament selection
    :param population: A list of tour objects
    :param mating_pool_size: mating pool size
    :param tournament_size: tournament size
    :return: individuals that selected into the mating pool
    """
    selected_to_mate = []
    mating_pool = random.sample(population, mating_pool_size)
    while len(selected_to_mate) < mating_pool_size:
        tour_pool = random.sample(mating_pool, tournament_size)
        best_fitness = -1
        best = tour_pool[0]
        shortest  = tour_pool[0].length
        for i in tour_pool:
            if i.length < shortest:
                shortest = i.length
                best = i
        selected_to_mate.append(best)
    return selected_to_mate


def mu_plus_lambda(cur_pop, offsprings):
    """
    Mu plus lambda selection
    :param cur_pop: current population
    :param offsprings: current offspring
    :return: new population
    """
    length = len(cur_pop)
    temp_pop = cur_pop + offsprings

    temp_pop = sorted(temp_pop, key=lambda item:item.length)
    pop = []
    for i in range(length):
        pop.append(temp_pop[i])

    return pop


