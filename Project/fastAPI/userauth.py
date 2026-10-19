# userauth.py

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base

# ================= DATABASE =================
DATABASE_URL = "postgresql://postgres:admin123@localhost:5432/TaskHub1"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(bind=engine)
Base = declarative_base()

# ================= MODEL =================
class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True)
    password = Column(String)
    role = Column(Integer)
    pending_task = Column(Integer)

Base.metadata.create_all(bind=engine)

# ================= FASTAPI =================
app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ================= REQUEST MODEL =================
class LoginRequest(BaseModel):
    username: str
    password: str

# ================= ROUTES =================

@app.get("/products")
def get_products():
    return [
        {"name": "Laptop", "price": 70000},
        {"name": "Mobile", "price": 20000},
        {"name": "Projector", "price": 22222},
        {"name": "Tablet", "price": 30000}
    ]

@app.get("/product")
def get_product():
    return [
        {"name": "Single Product", "price": 10000}
    ]

@app.post("/login")
def login(data: LoginRequest):
    db = SessionLocal()

    user = db.query(User).filter(
        User.username == data.username,
        User.password == data.password
    ).first()

    db.close()

    if user:
        return {
            "UserStatus": 1,
            "UserRole": user.role,
            "UserPendingTask": user.pending_task
        }
    else:
        return {
            "UserStatus": 0,
            "UserRole": 0,
            "UserPendingTask": 0
        }


@app.post("/register")
def register(data: LoginRequest):
    db = SessionLocal()

    existing_user = db.query(User).filter(User.username == data.username).first()
    if existing_user:
        db.close()
        return {"message": "User already exists"}

    new_user = User(
        username=data.username,
        password=data.password,
        role=2,
        pending_task=0
    )

    db.add(new_user)
    db.commit()
    db.close()

    return {"message": "User registered successfully"}