import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { type SelectChangeEvent } from '@mui/material/Select';

type SelectProps = {
  inputText: string;
  options: { label: string }[];
};

export default function SelectAutoWidth(props: SelectProps) {
  const [age, setAge] = React.useState('');

  const handleChange = (event: SelectChangeEvent) => {
    setAge(event.target.value);
  };

  return (
    <div>
      <FormControl
        className="select"
        sx={{
          minWidth: 150,
          '& .MuiInputLabel-root': {
            color: '#ccc', // color del label
          },
          '& .MuiOutlinedInput-root': {
            // bordes redondeados
            '& fieldset': {
              borderColor: '#888', // borde normal
            },
            '&:hover fieldset': {
              borderColor: '#1976d2', // borde al pasar el mouse
            },
            '&.Mui-focused fieldset': {
              borderColor: '#42a5f5', // borde cuando está enfocado
            },
          },
          '& .MuiSelect-select': {
            color: '#fff', // color del texto seleccionado
            backgroundColor: '#1e293b', // fondo del select
            padding: '10px 14px',
          },
          '& .MuiMenuItem-root': {
            backgroundColor: '#1e293b',
            color: '#fff',
            '&:hover': {
              backgroundColor: '#334155',
            },
          },
        }}
      >
        <InputLabel id="demo-simple-select-autowidth-label">{props.inputText}</InputLabel>
        <Select
          labelId="select-autowidth-label"
          id="select-autowidth"
          value={age}
          onChange={handleChange}
          autoWidth
          label="Edad"
        >
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
    </div>
  );
}
