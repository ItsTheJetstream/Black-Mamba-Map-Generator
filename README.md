# Hexagonal Map Generator

For a recent game project I started working on a map generator that can generate a hexagonal tiled map with biomes and countries. This project is a graphical user interface demonstrating what has been done so far.

*WARNING* The current state of the project does not allow for GUI use as I still work on it. Any inputs won't result in actual changes in the programs process!

## The Goal

The goal was to create a working generator with these functions:

* [x] generation of landmasses and water
* [x] handling seeds for the generation (a seed should always result in the very same map)
* [ ] graphical interface to change variables for the generation
* [ ] generation of biomes (user-defined)
* [ ] generation of countries (user-defined)
* [ ] export everything into saveable, reopenable files
* [x] image export


## What has been done so far

Currently this program can procedurally generate a map filled with water and land. This can then be turned into a hexagonal map. The program can currently also create biomes and countries and graphically display those. Sadly there are some bugs in this section and things I still have to fix so they are not fully completed yet.

## How does it work?

This system relies mainly on two algorithms to generate maps: Voronoi Diagrams ([Wikipedia](https://en.wikipedia.org/wiki/Voronoi_diagram)) and Simplex Noise ([Wikipedia](https://en.wikipedia.org/wiki/Simplex_noise)). The later is used to generate the actual map, meaning water and land whch can be seen on the right side of the following image.

![image1](https://preview.ibb.co/c8DPDc/saved.png "image 1")

After that a grid of user-defined hexagons is generated on top of the image by calculation the color of each hexagon by "counting" the amount of green and blue on the according position in the generated image. This procedure can be seen on the left side.

The mentioned Voronoi-Diagrams are used to generate the countries and biomes. This results in polygons that get then "filled" with a specific biome (see image 2). Same is used for the countries but this sadly still results in some countries being generated partly on another island (seperated by water), that's somethign I'm still trying to work around. One way to do this would be to use Lloyd's Algorithm([Wikipedia](https://en.wikipedia.org/wiki/Lloyd%27s_algorithm)) but this is very hard to do the way I currently generate the Voronoi Diagram. That's one of the things that will be added in the future.

![image2](https://preview.ibb.co/k5k7zH/mitbiomes.png "image 2")

## Thanks

Here I want to thank some of the persons that helped me to understand the principles of procedural map generation.

* Amit Patel ([Red Blob Games](https://www.redblobgames.com/)) for his amazing guides on [hexagonal grids](https://www.redblobgames.com/grids/hexagons/) and [map generation](https://www.redblobgames.com/maps/terrain-from-noise/)
* Stefan Gustavson for his [paper on simplex noise](http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf)
* Kurt Spencer for his [implementation of SimplexNoise](https://gist.github.com/KdotJPG/b1270127455a94ac5d19)
