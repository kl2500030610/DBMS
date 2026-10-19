print("Welcome to this session 911")


print("------------")


print("\n\n Name: Haarish Prajwal \n ID: 2500030610")

user_input = input("Enter a register number: ")

if user_input.isdigit():
    print("You entered:", user_input)
else:
    print("Invalid input. Please enter a valid register number.")



print("Character in your input :")
idx = 1
for char in user_input:
    
    print(f"Character {idx}: {char}")
    idx += 1

fruits = ["Apple", "Banana", "Cherry"]
for fruit in fruits:
    print(fruit)

for i in range(1, 10):
    print(i)

print("------------------------------")

for i in range(1, 10, 2):
    print(i)

print("------------------------------")

for idx in range(1, 10,):
    print("5 X ", idx, "=", 5 * idx)

print("------------------------------")

for i in range(3):
    for j in range(3):
        print(f"Outer loop iteration {i}, Inner loop iteration {j}")

print("------------------------------")

marks = 75
if marks >= 90:
    print("Grade: A")
elif marks >= 60:
    print("Grade: B")
else:
    print("Grade: C")

print(type(marks))

print("------------------------------")


v1 = 420
v2 = 69.96
v3 = "Konichiwa"
v4 = [1, 2, 3, 4, 5]
v5 = True
v6 = None

print(type(v1))
print(type(v2))
print(type(v3))
print(type(v4))
print(type(v5))
print(type(v6))

print("------------------------------")

products = [
    {"name": "Laptop", "price": 75000},
    {"name": "Smartphone", "price": 30000},
    {"name": "Tablet", "price": 40000},
    {"name": "Headphones", "price": 15000},
]

min_price = int(input("Enter the minimum price: "))
max_price = int(input("Enter the maximum price: "))

filtered = [p for p in products if min_price <= p["price"] <= max_price]

sorted_products = sorted(filtered, key=lambda x: x["price"])

for product in sorted_products:
    print(f"{product['name']}: {product['price']}")