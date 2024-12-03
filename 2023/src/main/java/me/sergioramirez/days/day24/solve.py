import sys
import sympy

xr, yr, zr, vxr, vyr, vzr = sympy.symbols(sys.argv[1])
equations = []

for i, (sx, sy, sz, vx, vy, vz) in enumerate([[int(str) for str in n.split(",")] for n in sys.argv[2].split("|")]):
    equations.append((xr - sx) * (vy - vyr) - (yr - sy) * (vx - vxr))
    equations.append((yr - sy) * (vz - vzr) - (zr - sz) * (vy - vyr))

answers = [soln for soln in sympy.solve(equations) if all(x % 1 == 0 for x in soln.values())]

answer = answers[0]

print(answer[xr] + answer[yr] + answer[zr])
