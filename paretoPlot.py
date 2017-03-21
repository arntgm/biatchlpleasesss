import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import sys


x = sys.argv[0]
print x
y = sys.argv[1]
z = sys.argv[2]
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.scatter(x, y, z, zdir='z', s=20, c=None, depthshade=True)
        ##fig, ax = plt.subplots()
plt.show()


##if __name__=='__main__':
  #  plotFront([2,3,4], [1,2,3], [5,6,7])
