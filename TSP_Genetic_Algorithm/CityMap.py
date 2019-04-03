import City
import Extract_cities

class CityMap(object):
    """
    A map which store all cities information of a given file

    self.city_map = {city id1: city object1, city id2: city object2, ...... }
    """
    def __init__(self,file):
        self.city_map = {}
        cities = Extract_cities.get_cities(file)
        for k,v in cities.items():
            id = k
            x = v[0]
            y = v[1]
            city = City.City(id, x, y, cities)
            city.cal_distance()
            self.city_map[id] = city


