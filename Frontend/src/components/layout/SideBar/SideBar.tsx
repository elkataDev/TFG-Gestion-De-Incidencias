import { NavLink } from 'react-router-dom';
import './SideBar.css';

type SideProps = {
  titulo: string;
};

const menuItems = [
  { name: 'Panel de control', icon: 'A', path: '/' },
  { name: 'Inventario', icon: 'C', path: '/inventario' },
  { name: 'Averías', icon: 'A', path: '/averias' },
  { name: 'Usuarios', icon: 'B', path: '/usuarios' },
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
              {item.name}
            </NavLink>
          ))}
        </nav>
      }
    </aside>
  );
}
