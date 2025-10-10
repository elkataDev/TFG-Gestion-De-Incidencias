import './Input.css';

import TextField from '@mui/material/TextField';

type InputProps = {
  label: string;
  placeholder?: string;
};

export const Input = ({ label }: InputProps) => {
  return <TextField id="outlined-basic" label={label} variant="outlined" />;
};
