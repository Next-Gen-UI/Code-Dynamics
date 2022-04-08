n =int(input())
for j in range(n):
    x= int(input())
    z =[1, 2];
    s =2
    while(z[-1]+z[-2]<=x):
        z.append(z[-2]+z[-1])
        if(z[-1]%2 == 0):
            s+=z[-1];
    print(s)
    