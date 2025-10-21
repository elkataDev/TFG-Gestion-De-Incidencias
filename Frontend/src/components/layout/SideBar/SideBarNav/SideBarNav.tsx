import { NavLink } from 'react-router-dom';

import { Home, Wrench, Package, Users } from 'lucide-react';

const menuItems = [
  { name: 'Panel de control', path: '/', icon: Home },
  { name: 'Inventario', path: '/inventario', icon: Package },
  { name: 'Averías', path: '/averias', icon: Wrench },
  { name: 'Usuarios', path: '/usuarios', icon: Users },
];

export default function SideBarNav() {
  return (
    <nav className="sidebar-nav">
      {menuItems.map((item) => {
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
