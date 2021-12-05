export class User {
  username!: string;
  password?: string | null;
  token?: string | null;
  admin: boolean = false;
}
