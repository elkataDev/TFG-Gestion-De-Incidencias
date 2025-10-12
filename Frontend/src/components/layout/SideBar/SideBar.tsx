import './SideBar.css';

type SideProps = {
  titulo: string;
  children: React.ReactNode;
};

export default function SideBar(props: SideProps) {
  return (
    <>
      <aside className="sidebar">
        <h3 className="sidebar-title">{props.titulo}</h3>
        {props.children}
      </aside>
    </>
  );
}
