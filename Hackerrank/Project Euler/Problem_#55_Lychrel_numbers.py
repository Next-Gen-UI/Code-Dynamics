n = int(input())

rank = dict()

def is_palindrome(x):
	return str(x) == str(x)[::-1]

def get_reverse(x):
	return int(str(x)[::-1])

for k in range(1, n+1):
	z = k
	for j in range(60):
		if(is_palindrome(z)):
			# print(k, z)
			if(z not in rank):
				rank[z] = 1
			else:
				rank[z] += 1
			break
		z = z + get_reverse(z)

ans = -1
for pali in rank:
	if(ans == -1 or rank[pali] > rank[ans]):
		ans = pali

print(ans, rank[ans])