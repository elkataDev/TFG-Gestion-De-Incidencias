import './App.css';
import SideBar from './components/layout/SideBar/SideBar';
import SideBarNav from './components/layout/SideBar/SideBarNav/SideBarNav';
import SideBarUser from './components/layout/SideBar/SideBarUser/SideBarUser';
function App() {
  return (
    <SideBar titulo="Gestor Incidencias">
      <SideBarNav></SideBarNav>
      <SideBarUser urlImg="" userName="Pepe" userEmail="correoEjemplo@gmail.com"></SideBarUser>
    </SideBar>
  );
}

export default App;
