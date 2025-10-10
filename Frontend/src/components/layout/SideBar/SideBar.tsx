import { NavLink } from 'react-router-dom';
import './SideBar.css';

import icon0 from './SideBarImgs/icon1.svg';
import icon1 from './SideBarImgs/icon2.svg';
import icon2 from './SideBarImgs/icon3.svg';
import icon3 from './SideBarImgs/icon4.svg';

type SideProps = {
  titulo: string;
};

const menuItems = [
  { name: 'Panel de control', path: '/', icon: icon0 },
  { name: 'Inventario', path: '/inventario', icon: icon1 },
  { name: 'Averías', path: '/averias', icon: icon2 },
  { name: 'Usuarios', path: '/usuarios', icon: icon3 },
];

export default function SideBar({ titulo }: SideProps) {
  return (
    <aside className="sidebar">
      <h3 className="sidebar-title">{titulo}</h3>
      {
        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
            >
              <img className="sidebar-icon" src={item.icon} alt="p   " />
              {item.name}
            </NavLink>
          ))}
        </nav>
      }
    </aside>
  );
}
