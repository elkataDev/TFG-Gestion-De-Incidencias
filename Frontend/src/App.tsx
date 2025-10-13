import './App.css';
import SideBar from './components/layout/SideBar/SideBar';
import SideBarNav from './components/layout/SideBar/SideBarNav/SideBarNav';
import SideBarUser from './components/layout/SideBar/SideBarUser/SideBarUser';
import ControlPanel from './pages/ControlPanel/ControlPanel';
import CP_Card from './pages/ControlPanel/CP_Card';

function App() {
  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      <SideBar titulo="Gestor Incidencias">
        <SideBarNav />
        <SideBarUser urlImg="" userName="Pepe" userEmail="correoEjemplo@gmail.com" />
      </SideBar>

      <div style={{ flex: 1, padding: '20px' }}>
        <ControlPanel titulo="Panel de control">
          <CP_Card titulo="Total de activos" numb={1250} />
          <CP_Card titulo="Activos disponibles" numb={980} />
          <CP_Card titulo="Activos en reparacion" numb={270} />
          <CP_Card
            titulo="Estado de averias"
            numb={150}
            progress={[
              { label: 'Pendientes', percent: 70 },
              { label: 'En proceso', percent: 90 },
              { label: 'Resueltas', percent: 70 },
            ]}
          ></CP_Card>
        </ControlPanel>
      </div>
    </div>
  );
}

export default App;
