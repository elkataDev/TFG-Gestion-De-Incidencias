import { NavLink } from 'react-router-dom';

import { Home, Wrench, Package, Users, Monitor } from 'lucide-react';

interface MenuItem {
  name: string;
  path: string;
  icon: typeof Home;
  roles: string[];
}

const menuItems: MenuItem[] = [
  { name: 'Panel de control', path: '/', icon: Home, roles: ['USUARIO', 'TECNICO', 'ADMIN'] },
  { name: 'Averías', path: '/averias', icon: Wrench, roles: ['USUARIO', 'TECNICO', 'ADMIN'] },
  { name: 'Inventario', path: '/inventario', icon: Package, roles: ['TECNICO', 'ADMIN'] },
  { name: 'Activos', path: '/activos', icon: Monitor, roles: ['TECNICO', 'ADMIN'] },
  { name: 'Usuarios', path: '/usuarios', icon: Users, roles: ['ADMIN'] },
];

export default function SideBarNav() {
  const role = localStorage.getItem('role') ?? 'USUARIO';

  return (
    <nav className="sidebar-nav">
      {menuItems
        .filter((item) => item.roles.includes(role))
        .map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
            >
              {({ isActive }) => (
                <>
                  <Icon className={`nav-icon ${isActive ? 'active' : ''}`} />
                  <span>{item.name}</span>
                </>
              )}
            </NavLink>
          );
        })}
    </nav>
  );
}
