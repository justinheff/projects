

def get_cities(cities_file):
    """
    A function to extract all the cities coordinates from the file
    :param file: city file
    :return: a dictionary which key is referenced to the city name and the value refer to the x, y coordinates
    """
    cities = {}
    with open(cities_file, "r") as file:
        for line in file:
            l = line.split(" ")
            name = int(l[0])
            x = float(l[1])
            y = float(l[2])
            cities[name] = [x, y]

    return cities
