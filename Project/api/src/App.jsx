import { useState } from "react";
import axios from "axios";

function App() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginResult, setLoginResult] = useState(null);

  // ================= FETCH PRODUCTS =================
  const fetchProduct = () => {
    setLoading(true);
    axios.get("http://127.0.0.1:8000/product")
      .then((response) => setData(response.data))
      .catch((error) => console.error(error))
      .finally(() => setLoading(false));
  };

  const fetchProducts = () => {
    setLoading(true);
    axios.get("http://127.0.0.1:8000/products")
      .then((response) => setData(response.data))
      .catch((error) => console.error(error))
      .finally(() => setLoading(false));
  };

  // ================= LOGIN =================
  const handleLogin = () => {
    axios.post("http://127.0.0.1:8000/login", {
      username: username,
      password: password
    })
    .then((res) => {
      setLoginResult(res.data);
    })
    .catch((err) => console.error(err));
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Products Dashboard</h1>

      {/* ================= PRODUCT BUTTONS ================= */}
      <button onClick={fetchProduct} style={{ marginRight: "10px" }}>
        Load Product
      </button>

      <button onClick={fetchProducts} style={{ marginRight: "10px" }}>
        Load Products
      </button>

      {/* ================= PRODUCT DISPLAY ================= */}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <ul>
          {data.map((item, index) => (
            <li key={index}>
              {item.name} - ₹{item.price}
            </li>
          ))}
        </ul>
      )}

      <hr />

      {/* ================= LOGIN SECTION ================= */}
      <h2>Login</h2>

      <input
        placeholder="Username"
        onChange={(e) => setUsername(e.target.value)}
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        onChange={(e) => setPassword(e.target.value)}
      />

      <br /><br />

      <button onClick={handleLogin}>Login</button>

      {/* ================= LOGIN RESULT ================= */}
      {loginResult && (
        <div style={{ marginTop: "20px" }}>
          <p>Status: {loginResult.UserStatus}</p>
          <p>Role: {loginResult.UserRole}</p>
          <p>Pending Tasks: {loginResult.UserPendingTask}</p>
        </div>
      )}
    </div>
  );
}

export default App;