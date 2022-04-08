n, k = [ int(i) for i in input().split()]
n += 1

ans = 0
pascal = [ [0 for j in range(n)] for i in range(n)]
for i in range(n):
	pascal[i][0] = 1
	pascal[i][i] = 1
	for j in range(1, i):
		pascal[i][j] = pascal[i-1][j-1] + pascal[i-1][j]
		if(pascal[i][j] > k):
			ans += 1

print(ans)

"""
for i in range(n):
	s = ""
	for j in range(n):
		s += " "+str(pascal[i][j])
	print(s)
"""
