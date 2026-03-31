import { Outlet } from 'react-router-dom';
import SideBar from '../SideBar/SideBar';
import SideBarNav from '../SideBar/SideBarNav/SideBarNav';
import SideBarUser from '../SideBar/SideBarUser/SideBarUser';
import LogoutButton from '@/components/common/BotonLogOut/BotonLogout';
import './MainLayout.css';

export default function MainLayout() {
  const userName = localStorage.getItem('username') ?? 'Usuario';
  const userRole = localStorage.getItem('role') ?? '';

  return (
    <div className="layout-container">
      <SideBar titulo="Gestor Incidencias">
        <SideBarNav />
        <SideBarUser urlImg="" userName={userName} userRole={userRole} />
        <LogoutButton />
      </SideBar>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
