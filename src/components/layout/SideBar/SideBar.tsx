import { useState } from 'react';
import './SideBar.css';

type SideProps = {
  titulo: string;
  children: React.ReactNode;
};

export default function SideBar(props: SideProps) {
  const [isOpen, setIsOpen] = useState<boolean>(true);

  return (
    <>
      <aside className={`sidebar ${isOpen ? '' : 'inactive'}`}>
        <span className="title-container">
          <h3 className="sidebar-title">{props.titulo}</h3>
          <button
            className="hamburger"
            onClick={() => {
              setIsOpen(!isOpen);
            }}
          >
            <span></span>
            <span></span>
            <span></span>
          </button>
        </span>

        {props.children}
      </aside>
    </>
  );
}
