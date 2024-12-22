function loginValidation() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  if (email === "" || !email.includes("@")) {
    alert('Invalid email!!');
  }

  if (password === "") {
    alert('Invalid password!!');
  }
}

function registrationValidation() {
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  // Validate name
  if (name === "") {
    alert('Name is required!');
    return false; // Prevent form submission
  }

  // Validate email
  if (email === "" || !email.includes("@")) {
    alert('Please enter a valid email!');
    return false; // Prevent form submission
  }

  // Validate password
  if (password === "") {
    alert('Password is required!');
    return false; // Prevent form submission
  }

  // Check if the password is strong enough (optional)
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; // Minimum 8 characters, at least one letter and one number
  if (!passwordRegex.test(password)) {
    alert('Password must be at least 8 characters long and contain both letters and numbers!');
    return false; // Prevent form submission
  }

  // If all validations pass, return true (form can be submitted)
  return true;
}