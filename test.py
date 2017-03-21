import sys
import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D

x = [1]
y = [2]
z = [3]
print len(sys.argv)
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.set_title(sys.argv[3].split(', '))
ax.scatter(x, y, z, zdir='z', s=20, c=None, depthshade=True)
        ##fig, ax = plt.subplots()
plt.show()
