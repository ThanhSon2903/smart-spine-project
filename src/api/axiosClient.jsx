import axios from "axios";
const axiosClient = axios.create({
    baseURL: "https://deloyonrailway-production.up.railway.app/api"
});

axiosClient.interceptors.request.use(
    (config) => {
     
        const token = localStorage.getItem("token");
        
        
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
     
        return config;
    },
    (error) => {
       
        return Promise.reject(error);
    }
);

export default axiosClient;