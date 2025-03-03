


// Import necessary modules
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-form',
  template: `
    <div class="container">
      <div class="auth-card">
        <h2 class="title">GROCERY STORE <br> <span>MANAGEMENT SYSTEM</span></h2>
        <h3 class="subtitle">ACCOUNT SIGN UP</h3>
        <form [formGroup]="authForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="username">Username</label>
            <input id="username" formControlName="username" placeholder="Enter username" />
            <div class="error" *ngIf="authForm.get('username')?.invalid && authForm.get('username')?.touched">
              Username is required
            </div>
          </div>

          <div class="form-group">
            <label for="password">Password *</label>
            <input id="password" formControlName="password" type="password" placeholder="Enter password" />
            <div class="error" *ngIf="authForm.get('password')?.invalid && authForm.get('password')?.touched">
              Password must be at least 6 characters
            </div>
          </div>

          <div *ngIf="isSignup" class="form-group">
            <label for="confirmPassword">Confirm Password *</label>
            <input id="confirmPassword" formControlName="confirmPassword" type="password" placeholder="Confirm password" />
            <div class="error" *ngIf="authForm.hasError('passwordMismatch') && authForm.get('confirmPassword')?.touched">
              Passwords do not match
            </div>
          </div>

          <button type="submit" [disabled]="authForm.invalid" class="btn">Sign Up</button>
        </form>
        <p *ngIf="!isSignup" class="link-text">Don't have an account? <a (click)="navigateToSignup()">Signup</a></p>
      </div>
    </div>
  `,
  styles: [
    `.container { display: flex; justify-content: center; align-items: center; height: 100vh; background: url('/assets/bg.jpg') no-repeat center center; background-size: cover; }`,
    `.auth-card { background: white; padding: 30px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); width: 400px; text-align: center; }`,
    `.title { font-size: 22px; font-weight: bold; margin-bottom: 10px; }`,
    `.title span { color: #007bff; }`,
    `.subtitle { font-size: 18px; margin-bottom: 20px; font-weight: 600; }`,
    `.form-group { margin-bottom: 15px; text-align: left; }`,
    `label { display: block; font-weight: 600; margin-bottom: 5px; }`,
    `input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; font-size: 14px; }`,
    `.error { color: red; font-size: 12px; margin-top: 5px; }`,
    `.btn { width: 100%; background-color: #007bff; color: white; padding: 10px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; }`,
    `.btn:disabled { background-color: #ccc; cursor: not-allowed; }`,
    `.link-text { margin-top: 15px; font-size: 14px; }`,
    `.link-text a { color: #007bff; cursor: pointer; text-decoration: underline; }`
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
