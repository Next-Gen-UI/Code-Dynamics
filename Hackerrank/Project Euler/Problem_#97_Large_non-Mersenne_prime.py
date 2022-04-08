t = int(raw_input())
M = 1000000000000
s = 0;

def expo(b, e):
	ans = 1
	t = b
	while(e != 0):
		if(e%2 == 1):
			ans = (ans*t)%M

		t = (t*t)%M
		e = e//2

	return ans

memo = dict()

for k in range(t):
	a, b, c, d = [int(i) for i in raw_input().split()]
	s = (s+d)%M;

	res = None
	if (b, c) in memo:
		res = memo[(b,c)]
	else:
		res = expo(b, c)
		memo[(b,c)] = res

	s = (s+(a*res)%M)%M;

print("%012d" % s)

