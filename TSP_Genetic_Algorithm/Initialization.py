import Tour

class Population(object):
    """
    Using dictionary to store a list of tour object

    Population:
        self.population = {1: [tour object1, fitness1], 2: [tour object2, fitness2],...... }
    """
    def __init__(self, size, city_map):
        """
        An initializer to initialize the population with a given size

        self.size: a size of the population
        self.city_map: a map that contains all the city information
        self.population: a list to store tour objects
        self.bestTour: the best tour in a population
        self.worstTour: the worst tour in a population
        self.AverageLength: the average length of tours in a population
        """
        self.size = size
        self.city_map = city_map
        self.population = []
        self.bestTour = None
        self.worstTour = None
        self.AverageLength = None

        for i in range(size):
            new_tour = Tour.Tour(self.city_map)
            self.population.append(new_tour)

    def evalPopulation(self):
        """
        Evaluate the population, to get all the information that need to be research.
        Also, put the best tour at the first index of the population (to better preserve diversity when running main)
        """
        self.bestTour = self.population[0]
        self.worstTour = self.population[0]
        offset = 0
        totalLength = 0
        for i in range(len(self.population)):
            totalLength += self.population[i].length
            if self.population[i].length < self.bestTour.length:
                self.bestTour = self.population[i]
                offset = i
            if self.population[i].length > self.worstTour.length:
                self.worstTour = self.population[i]

        self.AverageLength = totalLength/len(self.population)

        temp = self.population[offset]
        if offset != 0:
            self.population[offset] = self.population[0]
            self.population[0] = temp


