import Button from '@mui/material/Button';

type ButtonPrimarioProps = {
  text: string;
};

export default function ButtonPrimario({ text }: ButtonPrimarioProps) {
  return <Button variant="contained">{text}</Button>;
}
