import { useEffect, useRef, useState } from 'react';
import { imgurl, callApi } from './lib';
import './App.css';

const App = () => {
    const [isSignin, setIsSignIn] = useState(true);
    const finput = useRef();

    const [signinData, setSigninData] = useState({
        username: "",
        password: ""
    });

    const [signupData, setSignupData] = useState({
        username: "",
        password: "",
        role: 2,
        pending_task: 0
    });

    useEffect(()=>{
        finput.current.focus();
    },[]);

    function switchWindow(){
        setIsSignIn(prev => !prev);
        setTimeout(() => finput.current.focus(), 0);
    }

    // ================= SIGNIN =================
    function signin(){
        callApi(
            "POST",
            "http://127.0.0.1:8000/login",
            signinData,
            responseHandler
        );
    }

    // ================= SIGNUP =================
    function signup(){
        callApi(
            "POST",
            "http://127.0.0.1:8000/register",
            signupData,
            responseHandler
        );
    }

    // ================= RESPONSE =================
    function responseHandler(res){
        if(res.UserStatus === 1){
            alert(`Login Success\nRole: ${res.role || res.UserRole}\nPending: ${res.pending_task || res.UserPendingTask}`);
        } else if(res.message){
            alert(res.message);
        } else {
            alert("Something went wrong");
        }
    }

    return (
        <div className='app'>
            <div className='container' key={isSignin ? "signin" : "signup"}>
                <div className='container-header'>
                    <label>{isSignin ? "Join the Strawhat Crew": "Create Account"}</label>
                    <img src={imgurl + "jolly_roger_bg.png"} alt='' />
                </div>

                <div className='container-content'>

                    {isSignin? 
                        <>
                        <label>Pirate ID*</label>
                        <div className='input-group'>
                            <img src={imgurl + "user.png"} />
                            <input
                                type='text'
                                ref={finput}
                                placeholder='Enter username'
                                autoComplete='off'
                                onChange={(e)=>setSigninData({...signinData, username: e.target.value})}
                            />
                        </div>

                        <label>Password*</label>
                        <div className='input-group'>
                            <img src={imgurl + "padlock.png"} />
                            <input
                                type='password'
                                placeholder='Enter password'
                                onChange={(e)=>setSigninData({...signinData, password: e.target.value})}
                            />
                        </div>

                        <button onClick={signin}>Let's start</button>

                        <label onClick={switchWindow}>
                            Don't have a Pirate crew? <span>Start a new crew</span>
                        </label>
                        </>
                    :
                        <>
                        <label>Username*</label>
                        <div className='input-group'>
                            <img src={imgurl + "user.png"} />
                            <input
                                type='text'
                                ref={finput}
                                placeholder='Enter username'
                                autoComplete='off'
                                onChange={(e)=>setSignupData({...signupData, username: e.target.value})}
                            />
                        </div>

                        <label>Password*</label>
                        <div className='input-group'>
                            <img src={imgurl + "padlock.png"} />
                            <input
                                type='password'
                                placeholder='Enter password'
                                onChange={(e)=>setSignupData({...signupData, password: e.target.value})}
                            />
                        </div>

                        <button onClick={signup}>Register</button>

                        <label onClick={switchWindow}>
                            Already have crew? <span>Go on</span>
                        </label>
                        </>
                    }

                </div>

                <div className='container-footer'>
                    Copyright S9_30610 @ 2026. All rights reserved, Strawhats.
                </div>
            </div>
        </div>
    );
}

export default App;