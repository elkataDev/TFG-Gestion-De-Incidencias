import './Input.css';

type InputProps = {
  label: string;
  placeholder?: string;
};

export const Input = ({ label, placeholder }: InputProps) => {
  return (
    <div className="input-container">
      <label>{label}</label>
      <input type="text" placeholder={placeholder} />
    </div>
  );
};
