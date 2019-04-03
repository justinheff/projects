import random

class Tour(object):
    """
    Stores an ordered list of all cities information and the length of the tour
    """
    def __init__(self, city_map, tour=None):
        """
        An initializer to initialize the tour object

        self.city_map: a map that contains all the city information
        self.tour: a random shuffle list of all city id
        self.cities: a list that contains all the city id
        self.length: the length of the tour
        self.max_distance: the maximum distance between two cities in a tour
        self.worst_idx: the index between teo cities that have the longest distance in a tour
        self.city_objects: a list that store all the city objects
        """
        self.city_map = city_map
        self.cities = city_map.keys()
        self.city_objects = []
        self.max_distance = 0
        self.worst_idx = []
        if tour:
            self.tour = tour
            self.length = self.cal_len()
        else:
            self.tour = sorted(list(self.cities), key=lambda *args: random.random())
            self.length = self.cal_len()


    def cal_len(self):
        """
        A function to calculate the length of a tour and the max distance
        :return: the total length of the tour
        """
        tour_length = 0.0

        for city in self.tour:
            # add distance between current city to the next city
            self.city_objects.append(self.city_map[city])
            if city != self.tour[-1]:
                next_city = self.tour[self.tour.index(city)+1]
                distance_to_next = self.city_map[city].distances[next_city]
                if distance_to_next > self.max_distance:
                    self.max_distance = distance_to_next
                    self.worst_idx = [self.tour.index(city), self.tour.index(city)+1]
                tour_length += distance_to_next

            else:
                #add the distance between the last city and the first city
                start_city = self.tour[0]
                distance_to_next = self.city_map[city].distances[start_city]
                if distance_to_next > self.max_distance:
                    self.max_distance = distance_to_next
                    self.worst_idx = [self.tour.index(city), 0]
                tour_length += distance_to_next

        return tour_length


