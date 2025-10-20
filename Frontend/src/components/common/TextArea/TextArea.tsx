import TextareaAutosize from '@mui/material/TextareaAutosize';

type TextAreaProps = {
  minRows: number;
  placeHolder: string;
};

export default function TextArea(props: TextAreaProps) {
  return (
    <TextareaAutosize
      aria-label="minimum height"
      minRows={props.minRows}
      placeholder={props.placeHolder}
      className="text-area"
    />
  );
}
