const API_BASE_URL = (import.meta.env.VITE_API_URL as string) ?? 'http://localhost:5555/api';

export const getHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token.trim()}` } : {}),
  };
};

export default API_BASE_URL;
