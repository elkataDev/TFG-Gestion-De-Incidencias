import './ControlPanel.css';

type ControlPanelProps = {
  titulo: string;
  children: React.ReactNode;
};

export default function ControlPanel(props: ControlPanelProps) {
  return (
    <>
      <h1 className="control-panel-title">{props.titulo}</h1>
      <div className="breakdown-gen-container">{props.children}</div>
    </>
  );
}
