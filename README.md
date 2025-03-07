// Import necessary modules
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-form',
  template: `
    <form [formGroup]="authForm" (ngSubmit)="onSubmit()">
      <label for="username">Username:</label>
      <input id="username" formControlName="username" />
      <div *ngIf="authForm.get('username')?.invalid && authForm.get('username')?.touched">
        Username is required
      </div>

      <label for="password">Password:</label>
      <input id="password" formControlName="password" type="password" />
      <div *ngIf="authForm.get('password')?.invalid && authForm.get('password')?.touched">
        Password must be at least 6 characters
      </div>

      <ng-container *ngIf="isSignup">
        <label for="confirmPassword">Confirm Password:</label>
        <input id="confirmPassword" formControlName="confirmPassword" type="password" />
        <div *ngIf="authForm.hasError('passwordMismatch') && authForm.get('confirmPassword')?.touched">
          Passwords do not match
        </div>
      </ng-container>

      <button type="submit" [disabled]="authForm.invalid">{{ isSignup ? 'Sign Up' : 'Login' }}</button>
    </form>
    <p *ngIf="!isSignup">Don't have an account? <a (click)="navigateToSignup()">Signup</a></p>
  `,
  styles: [
    'input { display: block; margin-bottom: 10px; }',
    'div { color: red; font-size: 12px; }',
    'p { margin-top: 10px; cursor: pointer; color: blue; text-decoration: underline; }'
  ]
})
export class AuthFormComponent {
  @Input() isSignup = false;
  @Output() formSubmitted = new EventEmitter<FormGroup>();

  authForm: FormGroup;

  constructor(private router: Router) {
    this.authForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });

    if (this.isSignup) {
      this.authForm.addControl('confirmPassword', new FormControl('', Validators.required));
      this.authForm.setValidators(this.passwordMatchValidator);
    }
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.authForm.valid) {
      this.formSubmitted.emit(this.authForm);
    }
  }

  navigateToSignup() {
    this.router.navigate(['/signup']);
  }
}
