import { Outlet } from 'react-router-dom';
import SideBar from '../SideBar/SideBar';
import SideBarNav from '../SideBar/SideBarNav/SideBarNav';
import SideBarUser from '../SideBar/SideBarUser/SideBarUser';
import LogoutButton from '@/components/common/BotonLogOut/BotonLogout';
import './MainLayout.css';

export default function MainLayout() {
  return (
    <div className="layout-container">
      <SideBar titulo="Gestor Incidencias">
        <SideBarNav />
        <SideBarUser urlImg="" userName="Pepe" userEmail="correoEjemplo@gmail.com" />
        <LogoutButton></LogoutButton>
      </SideBar>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
