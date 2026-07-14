import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom"
import Login from "./pages/login"
// import ProtectedRoute from "./components/protectedRoute"
import Register from "./pages/register"
import VerifyOtp from "./pages/verifyOtp"
import Dashboard from "./pages/dashboard"
import "./App.css"
import Session from "./pages/Session"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login/>}></Route>
        <Route path="/register" element={<Register/>}></Route>
        <Route path="/verify-otp" element={<VerifyOtp />}/>
        <Route path="/dashboard" element={<Dashboard />}/>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/sessions" element = {<Session/>}/>

      </Routes>
    </BrowserRouter>
  )
}

export default App
