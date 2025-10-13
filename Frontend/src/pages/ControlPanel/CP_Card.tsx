type ProgressBar = {
  label: string;
  percent: number;
};

type CP_Card = {
  titulo: string;
  numb: number;
  progress?: ProgressBar[];
};

export default function CP_Card(props: CP_Card) {
  const hasProgress = props.progress && props.progress.length > 0;
  return (
    <div className="breakdown-gen-card">
      <div className="breakdown-gen-header">
        <span className="breakdown-gen-card-title"> {props.titulo}</span>
        <span className="breakdown-number">{props.numb}</span>
        <span className={`breakdown-subtext ${hasProgress ?? 'hidden'}`}>Total</span>
      </div>
      {hasProgress && (
        <div className="breakdown-progress">
          {props.progress?.map((item, index) => (
            <div key={index} className="progress-item">
              <div className="progress-labels">
                <span className="progress-text">{item.label}</span>
                <span className="progress-text">{item.percent}%</span>
              </div>
              <div className="progress-bar">
                <div className="progress-bar-fill" style={{ width: `${item.percent}%` }}></div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
