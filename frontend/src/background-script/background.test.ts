test('adds 1 + 2 to equal 3', () => {
  function myFunc(a: number, b: number) {
    return a + b
  }
  expect(myFunc(1, 2)).toBe(3)
})

export {}
