import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { type SelectChangeEvent } from '@mui/material/Select';
import './Select.css';

type SelectProps = {
  disabled?: boolean;
  inputText: string;
  options: { label: string }[];
  value?: string;
  onChange?: (value: string) => void;
};

export default function SelectAutoWidth(props: SelectProps) {
  const [selected, setSelected] = React.useState(props.value ?? '');

  const handleChange = (event: SelectChangeEvent) => {
    setSelected(event.target.value);
    if (props.onChange) props.onChange(event.target.value);
  };

  // Sincroniza si value externo cambia
  React.useEffect(() => {
    if (props.value !== undefined) setSelected(props.value);
  }, [props.value]);

  return (
    <FormControl className="select-blanco">
      <InputLabel id="select-autowidth-label">{props.inputText}</InputLabel>

      <Select labelId="select-autowidth-label" value={selected} onChange={handleChange} autoWidth>
        <MenuItem value="">
          <em>Ninguna</em>
        </MenuItem>
        {props.options.map((item, index) => (
          <MenuItem key={index} value={item.label}>
            {item.label}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
