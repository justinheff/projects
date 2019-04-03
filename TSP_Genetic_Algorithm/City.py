from math import sqrt

class City(object):
    """
    A city class which is use to store city object
    """

    def __init__(self, id, x, y, cities, distances=None):
        """
        Initialize city object, including the coordinates and the distances to other cities

        self.id: city id
        self.x: x coordinate
        self.y: y coordinate
        self.cities: a dictionary that contains citiies coordinates
        """
        self.id = id
        self.x = x
        self.y = y
        self.cities = cities

        self.distances = {}
        if distances:
            self.distances = distances


    def cal_distance(self):
        """
        A function to calculate the distance between salesman city and the other cities.
        Add the distance into the distances dictionary
        """
        for key, value in self.cities.items():
            point_dist = self.point_distance(self.x, self.y, value[0], value[1])
            self.distances[key] = point_dist


    def point_distance(self, x1, y1, x2, y2):
        """
        A function to calculate the Euclidean distance between two cities
        :param x1, y1: coordinates of the city that the salesman in
        :param x2, y2: coordinates of the destination city
        :return: Euclidean distance between teo cities
        """
        return sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2)



