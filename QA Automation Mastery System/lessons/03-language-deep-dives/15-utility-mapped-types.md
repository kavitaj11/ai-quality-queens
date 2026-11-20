# Utility & mapped types

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · TYPESCRIPT DEEP DIVE

TypeScript ships type-level functions that transform other types: Partial<T> (all optional — perfect for overrides), Required, Pick, Omit, Record, Readonly. Behind them are mapped types, which iterate a type's keys. These let test factories and fixtures reuse one source-of-truth type instead of hand-maintaining parallel shapes.

## Worked reference

````text
interface User {
  id: string;
  email: string;
  name: string;
  role: 'admin' | 'customer';
}

// Built-in utility types — no duplication needed:
type NewUser  = Omit<User, 'id'>;        // drop a field
type UserKeys = Pick<User, 'id' | 'email'>;
type Patch    = Partial<User>;           // all optional (overrides)
type Frozen   = Readonly<User>;          // no reassignment
type ByRole   = Record<User['role'], User[]>;  // { admin:[], customer:[] }

const patch: Patch = { role: 'admin' };  // valid partial

// A mapped type is what powers them — iterate the keys:
type Nullable<T> = { [K in keyof T]: T[K] | null };
type MaybeUser = Nullable<User>;         // every field | null
````

## Exercise

From one User type, derive NewUser (no id), UserPatch (all optional), and a Record<Role, User[]> grouping — with zero duplication.
