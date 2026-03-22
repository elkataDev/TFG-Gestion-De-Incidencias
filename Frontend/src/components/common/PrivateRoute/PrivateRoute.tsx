import { Navigate } from 'react-router-dom';
import React from 'react';

interface Props {
  children: React.ReactNode;
  allowedRoles?: string[];
}

export default function PrivateRoute({ children, allowedRoles }: Props) {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  // 1️⃣ NO LOGUEADO → LOGIN
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // 2️⃣ LOGUEADO PERO SIN ROL O ROL NO PERMITIDO → 403
  if (allowedRoles && (!role || !allowedRoles.includes(role))) {
    return <Navigate to="/unauthorized" replace />;
  }

  // 3️⃣ OK
  return children;
}
