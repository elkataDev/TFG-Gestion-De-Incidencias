import Button from '@mui/material/Button';

type ButtonSecundarioProps = {
  text: string;
};

export default function ButtonSecundario({ text }: ButtonSecundarioProps) {
  return <Button variant="outlined">{text}</Button>;
}
