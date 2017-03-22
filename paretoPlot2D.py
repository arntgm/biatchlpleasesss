import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.tri as triangles
from matplotlib import cm
import sys

nameMap = {"devi" : "Deviation", "conn" : "Connectivity", "edge" : "Edge Value"}
xname = nameMap[sys.argv[1]]
yname = nameMap[sys.argv[2]]
x = sys.argv[3].split(', ')
y = sys.argv[4].split(', ')
for i in range(len(x)):
    x[i] = float(x[i])
    y[i] = float(y[i])
fig = plt.figure()
fig.suptitle("Pareto Front")
plt.xlabel(xname)
plt.ylabel(yname)
plt.scatter(x, y)
        ##fig, ax = plt.subplots()
plt.show()


#if __name__=='__main__':
 #   
  #  plotFront([2, 3, 4])
