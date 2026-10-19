from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import random
from pydantic import BaseModel

# Create app ONLY ONCE
app = FastAPI()

# Enable CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # allow all (for development)
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ------------------ STATIC PRODUCT ------------------
@app.get("/product")
def get_product():
    return [
        {"name": "Laptop", "price": 75000},
        {"name": "Mobile", "price": 50000},
        {"name": "Tablet", "price": 30000},
        {"name": "Watch", "price": 15000}
    ]


# ------------------ RANDOM PRODUCTS ------------------
product_names = [
    "Laptop", "Mobile", "Tablet", "Smart Watch", "Headphones",
    "Camera", "Keyboard", "Mouse", "Monitor", "Speaker"
]

brands = [
    "Dell", "HP", "Apple", "Samsung", "Sony",
    "Lenovo", "Asus", "Acer", "Boat", "JBL"
]

@app.get("/products")
def get_products():
    products = []

    for i in range(1, 51):
        name = random.choice(product_names)
        brand = random.choice(brands)

        product = {
            "id": i,
            "name": f"{brand} {name}",
            "price": random.randint(1000, 100000),
            "rating": round(random.uniform(2.5, 5.0), 1),
            "stock": random.randint(0, 200),
            "category": random.choice(["Electronics", "Accessories"]),
            "in_stock": random.choice([True, False])
        }

        products.append(product)

    return products


# ------------------ LOGIN MODEL ------------------
class LoginRequest(BaseModel):
    username: str
    password: str


@app.post("/login")
def login(user: LoginRequest):
    if user.username == "admin" and user.password == "1234":
        return {
            "UserStatus": 1,
            "UserRole": 1,
            "UserPendingTask": 5
        }
    else:
        return {
            "UserStatus": 0,
            "UserRole": 0,
            "UserPendingTask": 0
        }


# ------------------ WELCOME ------------------
@app.get("/welcome")
def get_welcome():
    return "Hello, World!"


# uvicorn main:app --reload