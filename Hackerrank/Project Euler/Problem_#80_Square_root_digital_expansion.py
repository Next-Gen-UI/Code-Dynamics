from decimal import *
import math
N = int(input())
P = int(input())
getcontext().prec= P+5

ans = 0
for x in range(1, N+1):
	res = str(Decimal(x).sqrt())
	if(res.find(".") == -1):
		continue

	res = res.replace(".", "")

	#print(res, len(res))
	for i in range(0, P):
		ans += (ord(res[i])-ord('0'))

print(ans)


