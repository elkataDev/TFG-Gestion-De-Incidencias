const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const getHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

export default API_BASE_URL;
