import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import sys

print (len(sys.argv))
#x = np.array(map(float, sys.argv[1].split(', ')))
x = sys.argv[1].split(', ')
y = sys.argv[2].split(', ')
z = sys.argv[3].split(', ')
sys.stdout.write(sys.argv[0])
for i in range(len(x)):
    x[i] = float(x[i])
    y[i] = float(y[i])
    z[i] = float(z[i])
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.set_title("Pareto Front")
ax.set_xlabel("Deviation")
ax.set_ylabel("Connectivity")
ax.set_zlabel("Edge Value")
ax.scatter(x, y, z, zdir='z', s=20, c=None, depthshade=True)
        ##fig, ax = plt.subplots()
plt.show()


#if __name__=='__main__':
    
 #   plotFront([2, 3, 4])
