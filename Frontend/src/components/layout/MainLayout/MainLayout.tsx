import { Outlet } from 'react-router-dom';
import SideBar from '../SideBar/SideBar';
import SideBarNav from '../SideBar/SideBarNav/SideBarNav';
import SideBarUser from '../SideBar/SideBarUser/SideBarUser';
import './MainLayout.css';

export default function MainLayout() {
  return (
    <div className="layout-container">
      <SideBar titulo="Gestor Incidencias">
        <SideBarNav />
        <SideBarUser urlImg="" userName="Pepe" userEmail="correoEjemplo@gmail.com" />
      </SideBar>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
